/*
 * Copyright 2023 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package controllers

import base.SpecBase
import forms.MoreInformationFormProvider
import mocks.MockAppConfig
import mocks.services.MockUserAnswersService
import models.CancelReason.{Duplicate, Other}
import models.{NormalMode, UserAnswers}
import navigation.{FakeNavigator, Navigator}
import pages.{CancelReasonPage, MoreInformationPage}
import play.api.inject.bind
import play.api.test.FakeRequest
import play.api.test.Helpers._
import services.UserAnswersService
import views.html.MoreInformationView

import scala.concurrent.Future

class MoreInformationControllerSpec extends SpecBase with MockUserAnswersService with MockAppConfig {

  lazy val formProvider = new MoreInformationFormProvider()
  lazy val moreInformationRoute = routes.MoreInformationController.onPageLoad(testErn, testArc, NormalMode).url
  lazy val moreInformationSubmitAction = routes.MoreInformationController.onSubmit(testErn, testArc, NormalMode)

  val defaultUserAnswers = emptyUserAnswers.set(CancelReasonPage, Duplicate)

  class Fixture(val isMandatory: Boolean,
                val userAnswers: Option[UserAnswers] = Some(defaultUserAnswers)) {
    val application =
      applicationBuilder(userAnswers)
        .overrides(
          bind[Navigator].toInstance(new FakeNavigator(testOnwardRoute)),
          bind[UserAnswersService].toInstance(mockUserAnswersService)
        )
        .build()
    val form = formProvider(isMandatory)
    val view = application.injector.instanceOf[MoreInformationView]
  }

  "MoreInformation Controller" - {

    "must return OK and the correct view for a GET" in new Fixture(isMandatory = false) {
      running(application) {

        val request = FakeRequest(GET, moreInformationRoute)
        val result = route(application, request).value

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form, moreInformationSubmitAction, isMandatory)(dataRequest(request), messages(application)).toString
      }
    }

    "must populate the view correctly on a GET when the question has previously been answered" in new Fixture(
      isMandatory = false,
      userAnswers = Some(defaultUserAnswers.set(MoreInformationPage, Some("answer")))
    ) {
      running(application) {

        val request = FakeRequest(GET, moreInformationRoute)
        val result = route(application, request).value

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form.fill(Some("answer")), moreInformationSubmitAction, isMandatory)(dataRequest(request), messages(application)).toString
      }
    }

    "must redirect to the next page when valid data is submitted" in new Fixture(isMandatory = false) {
      running(application) {

      MockUserAnswersService.set().returns(Future.successful(defaultUserAnswers))

        val request = FakeRequest(POST, moreInformationRoute).withFormUrlEncodedBody(("value", "answer"))
        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual testOnwardRoute.url
      }
    }

    "must return a Bad Request and errors when invalid data is submitted" in new Fixture(
      isMandatory = true,
      userAnswers = Some(emptyUserAnswers.set(CancelReasonPage, Other))
    ) {
      running(application) {

        val request = FakeRequest(POST, moreInformationRoute).withFormUrlEncodedBody(("value", ""))
        val boundForm = formProvider(isMandatory = true).bind(Map("value" -> ""))
        val result = route(application, request).value

        status(result) mustEqual BAD_REQUEST
        contentAsString(result) mustEqual view(boundForm, moreInformationSubmitAction, isMandatory = true)(dataRequest(request), messages(application)).toString
      }
    }

    "must redirect to Journey Recovery for a GET if no existing data is found" in new Fixture(isMandatory = false, None) {
      running(application) {

        val request = FakeRequest(GET, moreInformationRoute)
        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual routes.JourneyRecoveryController.onPageLoad(testErn, testArc).url
      }
    }

    "must redirect to Journey Recovery for a POST if no existing data is found" in new Fixture(isMandatory = false, None) {
      running(application) {

        val request = FakeRequest(POST, moreInformationRoute).withFormUrlEncodedBody(("value", "answer"))
        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual routes.JourneyRecoveryController.onPageLoad(testErn, testArc).url
      }
    }
  }
}

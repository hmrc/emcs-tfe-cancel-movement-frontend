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
import forms.ChooseGiveMoreInformationFormProvider
import mocks.MockAppConfig
import mocks.services.MockUserAnswersService
import models.NormalMode
import navigation.{FakeNavigator, Navigator}
import pages.{ChooseGiveMoreInformationPage, MoreInformationPage}
import play.api.inject.bind
import play.api.mvc.Call
import play.api.test.FakeRequest
import play.api.test.Helpers._
import services.UserAnswersService
import views.html.ChooseGiveMoreInformationView

import scala.concurrent.Future

class ChooseGiveMoreInformationControllerSpec extends SpecBase with MockUserAnswersService with MockAppConfig {

  def onwardRoute = Call("GET", "/foo")

  val formProvider = new ChooseGiveMoreInformationFormProvider()
  val form = formProvider()

  lazy val chooseGiveMoreInformationRoute = routes.ChooseGiveMoreInformationController.onPageLoad(testErn, testArc, NormalMode).url
  lazy val chooseGiveMoreInformationSubmitAction = routes.ChooseGiveMoreInformationController.onSubmit(testErn, testArc, NormalMode)

  "ChooseGiveMoreInformation Controller" - {

    "must return OK and the correct view for a GET" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, chooseGiveMoreInformationRoute)

        val result = route(application, request).value

        val view = application.injector.instanceOf[ChooseGiveMoreInformationView]

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form, chooseGiveMoreInformationSubmitAction)(dataRequest(request), messages(application)).toString
      }
    }

    "must populate the view correctly on a GET when the question has previously been answered" in {

      val userAnswers = emptyUserAnswers.set(ChooseGiveMoreInformationPage, true)

      val application = applicationBuilder(userAnswers = Some(userAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, chooseGiveMoreInformationRoute)

        val view = application.injector.instanceOf[ChooseGiveMoreInformationView]

        val result = route(application, request).value

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form.fill(true), chooseGiveMoreInformationSubmitAction)(dataRequest(request), messages(application)).toString
      }
    }

    "must redirect to the next page when valid data is submitted (true)" in {

      MockUserAnswersService.set().returns(Future.successful(emptyUserAnswers.set(ChooseGiveMoreInformationPage, true)))

      val application =
        applicationBuilder(userAnswers = Some(emptyUserAnswers))
          .overrides(
            bind[Navigator].toInstance(new FakeNavigator(onwardRoute)),
            bind[UserAnswersService].toInstance(mockUserAnswersService)
          )
          .build()

      running(application) {
        val request =
          FakeRequest(POST, chooseGiveMoreInformationRoute)
            .withFormUrlEncodedBody(("value", "true"))

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual onwardRoute.url
      }
    }

    "must redirect to the next page when valid data is submitted (false) AND delete any existing data in the MoreInformation page" in {

      val answersBefore = emptyUserAnswers
        .set(ChooseGiveMoreInformationPage, true)
        .set(MoreInformationPage, Some("info"))

      val answerAfter = emptyUserAnswers.set(ChooseGiveMoreInformationPage, false)

      MockUserAnswersService.set(answerAfter).returns(Future.successful(answerAfter))

      val application =
        applicationBuilder(userAnswers = Some(answersBefore))
          .overrides(
            bind[Navigator].toInstance(new FakeNavigator(onwardRoute)),
            bind[UserAnswersService].toInstance(mockUserAnswersService)
          )
          .build()

      running(application) {
        val request =
          FakeRequest(POST, chooseGiveMoreInformationRoute)
            .withFormUrlEncodedBody(("value", "false"))

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual onwardRoute.url
      }
    }

    "must return a Bad Request and errors when invalid data is submitted" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request =
          FakeRequest(POST, chooseGiveMoreInformationRoute)
            .withFormUrlEncodedBody(("value", ""))

        val boundForm = form.bind(Map("value" -> ""))

        val view = application.injector.instanceOf[ChooseGiveMoreInformationView]

        val result = route(application, request).value

        status(result) mustEqual BAD_REQUEST
        contentAsString(result) mustEqual view(boundForm, chooseGiveMoreInformationSubmitAction)(dataRequest(request), messages(application)).toString
      }
    }

    "must redirect to Journey Recovery for a GET if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request = FakeRequest(GET, chooseGiveMoreInformationRoute)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual routes.JourneyRecoveryController.onPageLoad(testErn, testArc).url
      }
    }

    "must redirect to Journey Recovery for a POST if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request =
          FakeRequest(POST, chooseGiveMoreInformationRoute)
            .withFormUrlEncodedBody(("value", "true"))

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual routes.JourneyRecoveryController.onPageLoad(testErn, testArc).url
      }
    }
  }
}

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
import forms.CancelConfirmFormProvider
import mocks.MockAppConfig
import mocks.services.MockUserAnswersService
import navigation.{FakeNavigator, Navigator}
import pages.CancelConfirmPage
import play.api.Application
import play.api.data.Form
import play.api.http.Status.{OK, SEE_OTHER}
import play.api.inject.bind
import play.api.test.FakeRequest
import play.api.test.Helpers._
import services.UserAnswersService
import views.html.CancelConfirmView

import scala.concurrent.Future

class CancelConfirmControllerSpec extends SpecBase with MockUserAnswersService with MockAppConfig {

  trait Fixture {
    val application: Application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).overrides(
      bind[UserAnswersService].toInstance(mockUserAnswersService),
      bind[Navigator].toInstance(new FakeNavigator(testOnwardRoute, mockAppConfig))
    ).build()

    val formProvider = new CancelConfirmFormProvider()
    val form: Form[Boolean] = formProvider()
    val view: CancelConfirmView = application.injector.instanceOf[CancelConfirmView]
  }

  "CancelConfirmController" - {

    "when calling .onPageLoad()" - {

      "must render the view" in new Fixture {

        running(application) {
          val request = FakeRequest(GET, routes.CancelConfirmController.onPageLoad(testErn, testArc).url)

          val result = route(application, request).value

          status(result) mustEqual OK
          contentAsString(result) mustBe view(form)(dataRequest(request), messages(application)).toString()
        }
      }
    }

    "when calling .onSubmit()" - {

      "must render view with error when user does not select an option" in new Fixture {
        val boundForm: Form[Boolean] = form.bind(Map("value" -> "test"))

        running(application) {
          val request = FakeRequest(POST, routes.CancelConfirmController.onPageLoad(testErn, testArc).url)
            .withFormUrlEncodedBody(("value", "test"))

          val result = route(application, request).value

          status(result) mustEqual BAD_REQUEST
          contentAsString(result) mustBe view(boundForm)(dataRequest(request), messages(application)).toString()
        }
      }

      "must redirect to confirmation page when user selects yes" in new Fixture {

        running(application) {
          val updatedAnswers = emptyUserAnswers.set(CancelConfirmPage, true)
          MockUserAnswersService.set(updatedAnswers).returns(Future.successful(updatedAnswers)).once()

          val request = FakeRequest(POST, routes.CancelConfirmController.onSubmit(testErn, testArc).url)
            .withFormUrlEncodedBody(("value", "true"))


          val result = route(application, request).value


          status(result) mustEqual SEE_OTHER
          redirectLocation(result) mustBe Some(testOnwardRoute.url)
        }
      }

      "must redirect to at a glance page when user selects no" in new Fixture {

        running(application) {
          val updatedAnswers = emptyUserAnswers.set(CancelConfirmPage, false)
          MockUserAnswersService.set(updatedAnswers).returns(Future.successful(updatedAnswers)).once()

          val request = FakeRequest(POST, routes.CancelConfirmController.onSubmit(testErn, testArc).url)
            .withFormUrlEncodedBody(("value", "false"))

          val result = route(application, request).value

          status(result) mustEqual SEE_OTHER
          redirectLocation(result) mustBe Some(testOnwardRoute.url)
        }
      }
    }
  }
}

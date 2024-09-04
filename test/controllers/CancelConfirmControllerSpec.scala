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
import config.AppConfig
import fixtures.SubmitCancelMovementFixtures
import forms.CancelConfirmFormProvider
import handlers.ErrorHandler
import mocks.MockAppConfig
import mocks.services.{MockSubmitCancelMovementService, MockUserAnswersService}
import models.CancelReason.Other
import models.{ConfirmationDetails, MissingMandatoryPage, SubmitCancelMovementException, UserAnswers}
import navigation.{FakeNavigator, Navigator}
import pages.{CancelReasonPage, ConfirmationPage, MoreInformationPage}
import play.api.Application
import play.api.data.Form
import play.api.http.Status.{OK, SEE_OTHER}
import play.api.inject.bind
import play.api.test.FakeRequest
import play.api.test.Helpers._
import services.{SubmitCancelMovementService, UserAnswersService}
import views.html.CancelConfirmView

import scala.concurrent.Future

class CancelConfirmControllerSpec extends SpecBase
  with MockUserAnswersService
  with MockAppConfig
  with MockSubmitCancelMovementService
  with SubmitCancelMovementFixtures {

  class Fixture(val userAnswers: Option[UserAnswers] = Some(emptyUserAnswers)) {
    val application: Application = applicationBuilder(userAnswers).overrides(
      bind[UserAnswersService].toInstance(mockUserAnswersService),
      bind[Navigator].toInstance(new FakeNavigator(testOnwardRoute)),
      bind[SubmitCancelMovementService].toInstance(mockSubmitCancelMovementService)
    ).build()

    val formProvider = new CancelConfirmFormProvider()
    val form: Form[Boolean] = formProvider()
    val view: CancelConfirmView = application.injector.instanceOf[CancelConfirmView]
    val errorHandler = application.injector.instanceOf[ErrorHandler]
    val appConfig = application.injector.instanceOf[AppConfig]
  }

  "CancelConfirmController" - {

    "when calling .onPageLoad()" - {

      "must render the view" in new Fixture() {

        running(application) {
          val request = FakeRequest(GET, routes.CancelConfirmController.onPageLoad(testErn, testArc).url)

          val result = route(application, request).value

          status(result) mustEqual OK
          contentAsString(result) mustBe view(form, routes.CancelConfirmController.onSubmit(testErn, testArc))(dataRequest(request), messages(application)).toString()
        }
      }
    }

    "when calling .onSubmit()" - {

      "must render view with error when user does not select an option" in new Fixture() {
        val boundForm: Form[Boolean] = form.bind(Map("value" -> "test"))

        running(application) {
          val request = FakeRequest(POST, routes.CancelConfirmController.onPageLoad(testErn, testArc).url)
            .withFormUrlEncodedBody(("value", "test"))

          val result = route(application, request).value

          status(result) mustEqual BAD_REQUEST
          contentAsString(result) mustBe view(boundForm, routes.CancelConfirmController.onSubmit(testErn, testArc))(dataRequest(request), messages(application)).toString()
        }
      }

      "when user selects yes" - {

        "when valid data exists in order to be able to construct a valid submission" - {

          "when submission is successful" - {

            "save confirmation page details and redirect to onward route" in new Fixture(Some(
              emptyUserAnswers
                .set(CancelReasonPage, Other)
                .set(MoreInformationPage, Some("foo"))
            )) {

              running(application) {

                val updatedAnswers = userAnswers.get.set(ConfirmationPage, ConfirmationDetails(testConfirmationReference))
                MockUserAnswersService.set(updatedAnswers).returns(Future.successful(updatedAnswers)).once()
                MockSubmitCancelMovementService.submit(testErn, testArc, getMovementResponseModel, userAnswers.get)
                  .returns(Future.successful(successResponseChRIS))

                val request =
                  FakeRequest(POST, routes.CancelConfirmController
                    .onSubmit(testErn, testArc).url)
                    .withFormUrlEncodedBody(("value", "true"))

                val result = route(application, request).value

                status(result) mustEqual SEE_OTHER
                redirectLocation(result) mustBe Some(testOnwardRoute.url)
              }
            }
          }

          "when submission fails" - {

            "render ISE" in new Fixture(Some(
              emptyUserAnswers
                .set(CancelReasonPage, Other)
                .set(MoreInformationPage, Some("foo"))
            )) {

              running(application) {

                MockSubmitCancelMovementService.submit(testErn, testArc, getMovementResponseModel, userAnswers.get)
                  .returns(Future.failed(SubmitCancelMovementException("bang")))

                val request =
                  FakeRequest(POST, routes.CancelConfirmController
                    .onSubmit(testErn, testArc).url)
                    .withFormUrlEncodedBody(("value", "true"))

                val result = route(application, request).value

                status(result) mustBe INTERNAL_SERVER_ERROR
                contentAsString(result) mustBe errorHandler.internalServerErrorTemplate(request).toString()
              }
            }
          }
        }

        "when invalid data exists so the submission can NOT be generated" - {

          "must return BadRequest" in new Fixture(Some(emptyUserAnswers)) {

            running(application) {

              MockSubmitCancelMovementService.submit(testErn, testArc, getMovementResponseModel, emptyUserAnswers)
                .returns(Future.failed(MissingMandatoryPage("bang")))

              val request =
                FakeRequest(POST, routes.CancelConfirmController
                  .onSubmit(testErn, testArc).url)
                  .withFormUrlEncodedBody(("value", "true"))

              val result = route(application, request).value

              status(result) mustBe BAD_REQUEST
              contentAsString(result) mustBe errorHandler.badRequestTemplate(request).toString()
            }
          }
        }
      }

      "must redirect to account home page when user selects no" in new Fixture() {

        running(application) {

          val request =
            FakeRequest(POST, routes.CancelConfirmController
              .onSubmit(testErn, testArc).url)
              .withFormUrlEncodedBody(("value", "false"))

          val result = route(application, request).value

          status(result) mustEqual SEE_OTHER
          redirectLocation(result) mustBe Some(appConfig.emcsTfeHomeUrl(Some(testErn)))
        }
      }
    }
  }
}

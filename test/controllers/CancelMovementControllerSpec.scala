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
import mocks.services.MockUserAnswersService
import models.NormalMode
import play.api.http.Status.{OK, SEE_OTHER}
import play.api.inject.bind
import play.api.test.FakeRequest
import play.api.test.Helpers.{GET, POST, contentAsString, defaultAwaitTimeout, redirectLocation, route, running, status, writeableOf_AnyContentAsEmpty}
import services.UserAnswersService
import views.html.CancelMovementView

class CancelMovementControllerSpec extends SpecBase with MockUserAnswersService {

  "CancelMovementController" - {

    "when calling .onPageLoad()" - {

      "must render the view" in {

        val application = applicationBuilder(userAnswers = None).overrides(
          bind[UserAnswersService].toInstance(mockUserAnswersService)
        ).build()

        val view = application.injector.instanceOf[CancelMovementView]

        running(application) {
          val request = FakeRequest(GET, routes.CancelMovementController.onPageLoad(testErn, testArc).url)

          val result = route(application, request).value

          status(result) mustEqual OK
          contentAsString(result) mustBe view(testArc, routes.CancelMovementController.onSubmit(testErn, testArc))(request, messages(application)).toString()
        }
      }
    }

    "when calling .onSubmit()" - {

      "must redirect to next page" in {

        val application = applicationBuilder(userAnswers = None).overrides(
          bind[UserAnswersService].toInstance(mockUserAnswersService)
        ).build()

        running(application) {
          val request = FakeRequest(POST, routes.CancelMovementController.onSubmit(testErn, testArc).url)

          val result = route(application, request).value

          status(result) mustEqual SEE_OTHER
          redirectLocation(result) mustBe Some(routes.CancelReasonController.onPageLoad(testErn, testArc, NormalMode).url)
        }
      }
    }
  }
}

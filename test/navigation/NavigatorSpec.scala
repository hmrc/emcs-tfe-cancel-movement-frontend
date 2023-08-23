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

package navigation

import base.SpecBase
import controllers.routes
import models.CancelReason.{ContainsError, Other}
import models._
import pages._

class NavigatorSpec extends SpecBase {

  val navigator = new Navigator

  "Navigator" - {

    "in Normal mode" - {

      "must go from a page that doesn't exist in the route map to Index" in {
        case object UnknownPage extends Page

        navigator.nextPage(UnknownPage, NormalMode, emptyUserAnswers) mustBe
          routes.IndexController.onPageLoad(testErn, testArc)
      }

      "must go from CancelReasonPage" - {

        s"when Cancel Reason is $Other" - {

          //TODO: Update as part of future story to go to the GiveMoreInformation page
          "to the UnderConstruction page" in {
            navigator.nextPage(CancelReasonPage, NormalMode, emptyUserAnswers.set(CancelReasonPage, Other)) mustBe
              testOnly.controllers.routes.UnderConstructionController.onPageLoad()
          }
        }

        s"when Cancel Reason is NOT $Other" - {

          "to the ChooseGiveMoreInformationItem page" in {
            navigator.nextPage(CancelReasonPage, NormalMode, emptyUserAnswers.set(CancelReasonPage, ContainsError)) mustBe
              routes.ChooseGiveMoreInformationController.onPageLoad(testErn, testArc, NormalMode)
          }
        }
      }

      "must go from ChooseGiveMoreInformation" - {

        s"when answer is Yes (true)" - {

          //TODO: Update as part of future story to go to the GiveMoreInformation page
          "to the UnderConstruction page" in {
            navigator.nextPage(ChooseGiveMoreInformationPage, NormalMode, emptyUserAnswers.set(ChooseGiveMoreInformationPage, true)) mustBe
              testOnly.controllers.routes.UnderConstructionController.onPageLoad()
          }
        }

        s"when answer is No (false)" - {

          //TODO: Update as part of future story to go to CheckAnswers page
          "to the UnderConstruction page" in {
            navigator.nextPage(ChooseGiveMoreInformationPage, NormalMode, emptyUserAnswers.set(ChooseGiveMoreInformationPage, false)) mustBe
              testOnly.controllers.routes.UnderConstructionController.onPageLoad()
          }
        }
      }
    }

    "in Check mode" - {


    }
  }
}

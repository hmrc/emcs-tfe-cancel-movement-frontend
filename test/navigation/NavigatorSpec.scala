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
import models.CancelReason.{ContainsError, Duplicate, Other}
import models._
import pages._

class NavigatorSpec extends SpecBase {

  val navigator = new Navigator()

  "Navigator" - {

    "in Normal mode" - {

      "from a page that doesn't exist in the route map to Index" in {
        case object UnknownPage extends Page

        navigator.nextPage(UnknownPage, NormalMode, emptyUserAnswers) mustBe
          routes.IndexController.onPageLoad(testErn, testArc)
      }

      "from CancelReasonPage" - {

        s"when Cancel Reason is $Other" - {

          "to the MoreInformation page" in {
            navigator.nextPage(CancelReasonPage, NormalMode, emptyUserAnswers.set(CancelReasonPage, Other)) mustBe
              routes.MoreInformationController.onPageLoad(testErn, testArc, NormalMode)
          }
        }

        s"when Cancel Reason is NOT $Other" - {

          "to the ChooseGiveMoreInformationItem page" in {
            navigator.nextPage(CancelReasonPage, NormalMode, emptyUserAnswers.set(CancelReasonPage, ContainsError)) mustBe
              routes.ChooseGiveMoreInformationController.onPageLoad(testErn, testArc, NormalMode)
          }
        }
      }

      "from ChooseGiveMoreInformation" - {

        s"when answer is Yes (true)" - {

          "to the MoreInformation page" in {
            navigator.nextPage(ChooseGiveMoreInformationPage, NormalMode, emptyUserAnswers.set(ChooseGiveMoreInformationPage, true)) mustBe
              routes.MoreInformationController.onPageLoad(testErn, testArc, NormalMode)
          }
        }

        "when answer is No (false)" - {

          "to the CheckYourAnswers page" in {
            navigator.nextPage(ChooseGiveMoreInformationPage, NormalMode, emptyUserAnswers.set(ChooseGiveMoreInformationPage, false)) mustBe
              routes.CheckYourAnswersController.onPageLoad(testErn, testArc)
          }
        }
      }

      "from MoreInformation page" - {

        "to the CheckYourAnswers page" in {
          navigator.nextPage(MoreInformationPage, NormalMode, emptyUserAnswers) mustBe
            routes.CheckYourAnswersController.onPageLoad(testErn, testArc)
        }
      }

      "from CheckYourAnswers page" - {

        "to the CancelConfirm page" in {
          navigator.nextPage(CheckYourAnswersPage, NormalMode, emptyUserAnswers) mustBe
            routes.CancelConfirmController.onPageLoad(testErn, testArc)
        }
      }

      "must go from CancelConfirm page" - {

        "to the confirmation page" in {
          navigator.nextPage(CancelConfirmPage, NormalMode, emptyUserAnswers.set(CancelConfirmPage, true)) mustBe
            routes.ConfirmationController.onPageLoad(testErn, testArc)
        }
      }

      "must go from CancelConfirm page" - {

        "to the ConfirmationPage" in {
          navigator.nextPage(CancelConfirmPage, NormalMode, emptyUserAnswers.set(CancelConfirmPage, true)) mustBe
            routes.ConfirmationController.onPageLoad(testErn, testArc)
        }
      }
    }

    "in Check mode" - {

      "from the CancelReason page" - {

        "if reason is 'Other'" - {

          "if the MoreInformation page has NO value" - {

            "must go to the MoreInformation page" in {
              navigator.nextPage(CancelReasonPage, CheckMode, emptyUserAnswers.set(CancelReasonPage, Other)) mustBe
                routes.MoreInformationController.onPageLoad(testErn, testArc, NormalMode)
            }
          }

          "if the MoreInformation page has a value" - {

            "must go to the CheckYourAnswers page" in {
              navigator.nextPage(CancelReasonPage, CheckMode, emptyUserAnswers.set(CancelReasonPage, Other).set(MoreInformationPage, Some("foo"))) mustBe
                routes.CheckYourAnswersController.onPageLoad(testErn, testArc)
            }
          }
        }

        "if reason is NOT 'Other'" - {

          "must go to the CheckYourAnswers page" in {
            navigator.nextPage(CancelReasonPage, CheckMode, emptyUserAnswers.set(CancelReasonPage, Duplicate)) mustBe
              routes.CheckYourAnswersController.onPageLoad(testErn, testArc)
          }
        }
      }

      "from the MoreInformationPage page" - {

        "must go to the CheckYourAnswers page" in {
          navigator.nextPage(MoreInformationPage, CheckMode, emptyUserAnswers) mustBe
            routes.CheckYourAnswersController.onPageLoad(testErn, testArc)
        }
      }
    }
  }
}

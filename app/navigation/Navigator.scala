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

import config.AppConfig
import controllers.routes
import models.CancelReason.Other
import models.{Mode, NormalMode, UserAnswers}
import pages._
import play.api.mvc.Call
import utils.JsonOptionFormatter._

import javax.inject.{Inject, Singleton}

@Singleton
class Navigator @Inject()(appConfig: AppConfig) extends BaseNavigator {

  private val normalRoutes: Page => UserAnswers => Call = {
    case CancelReasonPage => (userAnswers: UserAnswers) =>
      userAnswers.get(CancelReasonPage) match {
        case Some(Other) =>
          routes.MoreInformationController.onPageLoad(userAnswers.ern, userAnswers.arc, NormalMode)
        case _ =>
          routes.ChooseGiveMoreInformationController.onPageLoad(userAnswers.ern, userAnswers.arc, NormalMode)
      }
    case ChooseGiveMoreInformationPage => (userAnswers: UserAnswers) =>
      userAnswers.get(ChooseGiveMoreInformationPage) match {
        case Some(true) =>
          routes.MoreInformationController.onPageLoad(userAnswers.ern, userAnswers.arc, NormalMode)
        case _ =>
          routes.CheckYourAnswersController.onPageLoad(userAnswers.ern, userAnswers.arc)
      }
    case MoreInformationPage => (userAnswers: UserAnswers) =>
      routes.CheckYourAnswersController.onPageLoad(userAnswers.ern, userAnswers.arc)
    case CheckYourAnswersPage => (userAnswers: UserAnswers) =>
      routes.CancelConfirmController.onPageLoad(userAnswers.ern, userAnswers.arc, NormalMode)
    case CancelConfirmPage => (userAnswers: UserAnswers) =>
      userAnswers.get(CancelConfirmPage) match {
        case Some(true) =>
          //TODO: Route to ConfirmationPage as part of future story
          testOnly.controllers.routes.UnderConstructionController.onPageLoad()
        case _ =>
          Call("GET", appConfig.emcsTfeHomeUrl(Some(userAnswers.ern)))
      }
    case _ => (userAnswers: UserAnswers) =>
      routes.IndexController.onPageLoad(userAnswers.ern, userAnswers.arc)
  }

  private val checkRoutes: Page => UserAnswers => Call = {
    case CancelReasonPage => (userAnswers: UserAnswers) =>
      (userAnswers.get(CancelReasonPage), userAnswers.get(MoreInformationPage).flatten) match {
        case (Some(Other), None) => routes.MoreInformationController.onPageLoad(userAnswers.ern, userAnswers.arc, NormalMode)
        case _ => routes.CheckYourAnswersController.onPageLoad(userAnswers.ern, userAnswers.arc)
      }
    case _ => (userAnswers: UserAnswers) =>
      routes.CheckYourAnswersController.onPageLoad(userAnswers.ern, userAnswers.arc)
  }

  override def nextPage(page: Page, mode: Mode, userAnswers: UserAnswers): Call = mode match {
    case NormalMode => normalRoutes(page)(userAnswers)
    case _ => checkRoutes(page)(userAnswers)
  }
}

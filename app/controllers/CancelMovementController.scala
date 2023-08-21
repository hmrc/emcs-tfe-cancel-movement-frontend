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

import navigation.Navigator
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import services.UserAnswersService
import views.html.CancelMovementView
import controllers.actions.{AuthAction, DataRetrievalAction, MovementAction}

import javax.inject.Inject
import scala.concurrent.Future

class CancelMovementController @Inject()(override val messagesApi: MessagesApi,
                                         override val navigator: Navigator,
                                         override val userAnswersService: UserAnswersService,
                                         val controllerComponents: MessagesControllerComponents,
                                         view: CancelMovementView
                                        ) extends BaseNavigationController with I18nSupport {

  def onPageLoad(ern: String, arc: String): Action[AnyContent] = Action { implicit request =>
    Ok(view(arc, controllers.routes.CancelMovementController.onSubmit(ern, arc)))
  }

  def onSubmit(ern: String, arc: String): Action[AnyContent] = Action { implicit request =>
    Redirect(testOnly.controllers.routes.UnderConstructionController.onPageLoad())
  }
}
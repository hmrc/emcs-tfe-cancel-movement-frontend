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

import config.AppConfig
import controllers.actions._
import forms.CancelConfirmFormProvider
import models.requests.DataRequest
import models.{ConfirmationDetails, NormalMode}
import navigation.Navigator
import pages.{CancelConfirmPage, ConfirmationPage}
import play.api.data.Form
import play.api.i18n.MessagesApi
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents, Result}
import services.UserAnswersService
import views.html.CancelConfirmView

import javax.inject.Inject
import scala.concurrent.Future

class CancelConfirmController @Inject()(override val messagesApi: MessagesApi,
                                        override val userAnswersService: UserAnswersService,
                                        override val navigator: Navigator,
                                        override val auth: AuthAction,
                                        override val withMovement: MovementAction,
                                        override val getData: DataRetrievalAction,
                                        override val requireData: DataRequiredAction,
                                        override val userAllowList: UserAllowListAction,
                                        formProvider: CancelConfirmFormProvider,
                                        val controllerComponents: MessagesControllerComponents,
                                        view: CancelConfirmView
                                       )(implicit appConfig: AppConfig) extends BaseNavigationController with AuthActionHelper {

  def onPageLoad(ern: String, arc: String): Action[AnyContent] =
    authorisedDataRequestWithCachedMovementAsync(ern, arc) { implicit request =>
      renderView(Ok, formProvider())
    }

  def onSubmit(ern: String, arc: String): Action[AnyContent] =
    authorisedDataRequestWithCachedMovementAsync(ern, arc) { implicit request =>
      formProvider().bindFromRequest().fold(
        renderView(BadRequest, _),
        {
          case true =>
            //TODO: Update to call submission service as part of future story
            save(ConfirmationPage, ConfirmationDetails("DUMMY_RECEIPT_REFERENCE")).map { answers =>
              Redirect(navigator.nextPage(CancelConfirmPage, NormalMode, answers))
            }
          case false =>
            Future.successful(Redirect(appConfig.emcsTfeHomeUrl(Some(ern))))
        }
      )
    }

  private def renderView(status: Status, form: Form[_])(implicit request: DataRequest[_]): Future[Result] =
    Future.successful(status(view(form, routes.CancelConfirmController.onSubmit(request.ern, request.arc))))
}

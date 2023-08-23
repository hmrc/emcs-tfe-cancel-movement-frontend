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

import controllers.actions._
import forms.MoreInformationFormProvider
import models.CancelReason.Other
import models.Mode
import models.requests.DataRequest
import navigation.Navigator
import pages.{CancelReasonPage, MoreInformationPage}
import play.api.data.Form
import play.api.i18n.MessagesApi
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents, Result}
import services.UserAnswersService
import utils.JsonOptionFormatter
import views.html.MoreInformationView

import javax.inject.Inject
import scala.concurrent.Future

class MoreInformationController @Inject()(
                                           override val messagesApi: MessagesApi,
                                           override val userAnswersService: UserAnswersService,
                                           override val navigator: Navigator,
                                           override val auth: AuthAction,
                                           override val withMovement: MovementAction,
                                           override val getData: DataRetrievalAction,
                                           override val requireData: DataRequiredAction,
                                           override val userAllowList: UserAllowListAction,
                                           formProvider: MoreInformationFormProvider,
                                           val controllerComponents: MessagesControllerComponents,
                                           view: MoreInformationView
                                         ) extends BaseNavigationController with AuthActionHelper with JsonOptionFormatter {

  def onPageLoad(ern: String, arc: String, mode: Mode): Action[AnyContent] =
    authorisedDataRequestWithCachedMovementAsync(ern, arc) { implicit request =>
      withFormAndMandatoryFlag { (form, isMandatory) =>
        renderView(Ok, fillForm(MoreInformationPage, form), isMandatory, mode)
      }
    }

  def onSubmit(ern: String, arc: String, mode: Mode): Action[AnyContent] =
    authorisedDataRequestWithCachedMovementAsync(ern, arc) { implicit request =>
      withFormAndMandatoryFlag { (form, isMandatory) =>
        form.bindFromRequest().fold(
          renderView(BadRequest, _, isMandatory, mode),
          saveAndRedirect(MoreInformationPage, _, mode)
        )
      }
    }

  private def withFormAndMandatoryFlag(f: (Form[Option[String]], Boolean) => Future[Result])(implicit request: DataRequest[_]): Future[Result] =
    withAnswer(CancelReasonPage) { reason =>
      val isMandatory = reason == Other
      f(formProvider(isMandatory), isMandatory)
    }

  private def renderView(status: Status, form: Form[_], isMandatory: Boolean, mode: Mode)(implicit request: DataRequest[_]): Future[Result] =
    Future.successful(status(view(form, routes.MoreInformationController.onSubmit(request.ern, request.arc, mode), isMandatory)))
}

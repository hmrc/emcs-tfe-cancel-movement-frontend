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

package views

import base.ViewSpecBase
import fixtures.messages.CancelConfirmMessages
import forms.CancelConfirmFormProvider
import models.requests.DataRequest
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import play.api.i18n.Messages
import play.api.mvc.AnyContentAsEmpty
import play.api.test.FakeRequest
import views.html.CancelConfirmView

class CancelConfirmViewSpec extends ViewSpecBase with ViewBehaviours {

  object Selectors extends BaseSelectors

  "CancelConfirmView" - {

    Seq(CancelConfirmMessages.English, CancelConfirmMessages.Welsh).foreach { messagesForLanguage =>

      s"when being rendered in lang code of '${messagesForLanguage.lang.code}'" - {

        implicit val msgs: Messages = messages(app, messagesForLanguage.lang)
        implicit val request: DataRequest[AnyContentAsEmpty.type] = dataRequest(FakeRequest(), emptyUserAnswers)

        lazy val view = app.injector.instanceOf[CancelConfirmView]
        lazy val form = app.injector.instanceOf[CancelConfirmFormProvider].apply()

        implicit val doc: Document = Jsoup.parse(view(form).toString())

        // scalastyle:off magic.number
        behave like pageWithExpectedElementsAndMessages(Seq(
          Selectors.title -> messagesForLanguage.title,
          Selectors.h1 -> messagesForLanguage.heading,
          Selectors.h2(1) -> messagesForLanguage.arcSubheading(testArc),
          Selectors.p(1) -> messagesForLanguage.paragraph1,
          Selectors.p(2) -> messagesForLanguage.paragraph2,
          Selectors.radioButton(1) -> messagesForLanguage.radioButton1,
          Selectors.radioButton(2) -> messagesForLanguage.radioButton2,
          Selectors.button -> messagesForLanguage.continue
        ))
      }
    }
  }
}
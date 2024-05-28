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

package fixtures.messages

import fixtures.i18n


object ConfirmationMessages {

  sealed trait ViewMessages { _: i18n =>
    val heading: String
    val title: String
    val delayInformationH2: String
    val print: String
    val mayNeedToDoH2: String
    val mayNeedToDoP1: String
    val returnToAtAGlanceButton: String
    val feedback: String
  }
  object English extends ViewMessages with BaseEnglish {
    val heading: String = "Cancellation submitted"
    val title: String = titleHelper(heading)
    val delayInformationH2: String = "Cancellation information"
    val print: String = "Print this screen to make a record of your submission."
    val mayNeedToDoH2: String = "What you may need to do"
    val mayNeedToDoP1: String = "If you still want to move the goods, you will need to create a new movement with a different local reference number (LRN)."
    val returnToAtAGlanceButton: String = "Return to at a glance"
    val feedback: String = "What did you think of this service? (opens in new tab) (takes 30 seconds)"
  }

}

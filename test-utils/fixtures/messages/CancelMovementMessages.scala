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

object CancelMovementMessages {

  sealed trait ViewMessages { _: i18n =>
    val title: String
    val heading: String
    def caption(arc: String): String
    val paragraph1: String
    val paragraph2: String
    val bullet1: String
    val bullet2: String
  }

  object English extends ViewMessages with BaseEnglish {
    override val heading = "Excise Movement and Control System - GOV.UK"
    override val title = titleHelper("Cancel this movement")
    override def caption(arc: String) = s"Cancellation for $arc"
    override val paragraph1 = "You can choose to cancel a movement before the goods have left your premises."
    override val paragraph2 = "By cancelling this movement, you will also permanently delete its:"
    override val bullet1 = "electronic administrative document (eAD)"
    override val bullet2 = "administrative reference code (ARC)"

  }

  object Welsh extends ViewMessages with BaseWelsh {
    override val heading = "Excise Movement and Control System - GOV.UK"
    override val title = titleHelper("Cancel this movement")
    override def caption(arc: String) = s"Cancellation for $arc"
    override val paragraph1 = "You can choose to cancel a movement before the goods have left your premises."
    override val paragraph2 = "By cancelling this movement, you will also permanently delete its:"
    override val bullet1 = "electronic administrative document (eAD)"
    override val bullet2 = "administrative reference code (ARC)"
  }
}
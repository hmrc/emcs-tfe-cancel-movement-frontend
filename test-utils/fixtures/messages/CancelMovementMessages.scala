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
    val paragraph1: String
    val paragraph2: String
    val paragraph3: String
  }

  object English extends ViewMessages with BaseEnglish {
    override val heading = "Cancel this movement"
    override val title = titleHelper(heading)
    override val paragraph1 = "You can choose to cancel a movement before the goods have left your premises."
    override val paragraph2 = "When you cancel a movement, its electronic administrative document (eAD) and administrative reference code (ARC) are saved and marked as cancelled."
    override val paragraph3 = "If you still want to move the goods, you will need to create a new movement."
  }

  object Welsh extends ViewMessages with BaseWelsh {
    override val heading = "Cancel this movement"
    override val title = titleHelper(heading)
    override val paragraph1 = "You can choose to cancel a movement before the goods have left your premises."
    override val paragraph2 = "When you cancel a movement, its electronic administrative document (eAD) and administrative reference code (ARC) are saved and marked as cancelled."
    override val paragraph3 = "If you still want to move the goods, you will need to create a new movement."
  }
}
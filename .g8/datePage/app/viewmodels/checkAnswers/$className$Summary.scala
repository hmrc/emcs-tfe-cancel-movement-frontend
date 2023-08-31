package viewmodels.checkAnswers

import java.time.format.DateTimeFormatter

import controllers.routes
import models.{CheckMode, UserAnswers}
import pages.$className$Page
import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryListRow
import viewmodels.govuk.summarylist._
import viewmodels.implicits._

object $className$Summary  {

  def row(answers: UserAnswers)(implicit messages: Messages): Option[SummaryListRow] =
    answers.get($className$Page).map {
      answer =>

        val dateFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy")

        SummaryListRowViewModel(
          key     = "$className;format="decap"$.checkYourAnswers.label",
          value   = ValueViewModel(answer.format(dateFormatter)),
          actions = Seq(
            ActionItemViewModel("site.change", routes.$className$Controller.onPageLoad(answers.ern, answers.arc, CheckMode).url, $className$Page)
              .withVisuallyHiddenText(messages("$className;format="decap"$.change.hidden"))
          )
        )
    }
}

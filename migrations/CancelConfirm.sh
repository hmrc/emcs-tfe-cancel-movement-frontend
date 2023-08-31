#!/bin/bash

echo ""
echo "Applying migration CancelConfirm"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /trader/:ern/movement/:arc/cancelConfirm                        controllers.CancelConfirmController.onPageLoad(ern: String, arc: String, mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /trader/:ern/movement/:arc/cancelConfirm                        controllers.CancelConfirmController.onSubmit(ern: String, arc: String, mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /trader/:ern/movement/:arc/CancelConfirm/change                  controllers.CancelConfirmController.onPageLoad(ern: String, arc: String, mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /trader/:ern/movement/:arc/CancelConfirm/change                  controllers.CancelConfirmController.onSubmit(ern: String, arc: String, mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "cancelConfirm.title = cancelConfirm" >> ../conf/messages.en
echo "cancelConfirm.heading = cancelConfirm" >> ../conf/messages.en
echo "cancelConfirm.checkYourAnswersLabel = cancelConfirm" >> ../conf/messages.en
echo "cancelConfirm.error.required = Select yes if cancelConfirm" >> ../conf/messages.en
echo "cancelConfirm.change.hidden = CancelConfirm" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "cancelConfirm.title = cancelConfirm" >> ../conf/messages.cy
echo "cancelConfirm.heading = cancelConfirm" >> ../conf/messages.cy
echo "cancelConfirm.checkYourAnswersLabel = cancelConfirm" >> ../conf/messages.cy
echo "cancelConfirm.error.required = Select yes if cancelConfirm" >> ../conf/messages.cy
echo "cancelConfirm.change.hidden = CancelConfirm" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryCancelConfirmUserAnswersEntry: Arbitrary[(CancelConfirmPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[CancelConfirmPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryCancelConfirmPage: Arbitrary[CancelConfirmPage.type] =";\
    print "    Arbitrary(CancelConfirmPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(CancelConfirmPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration CancelConfirm completed"

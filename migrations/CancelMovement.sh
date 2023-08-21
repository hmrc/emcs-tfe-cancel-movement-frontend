#!/bin/bash

echo ""
echo "Applying migration CancelMovement"

echo "Adding routes to conf/app.routes"
echo "" >> ../conf/app.routes
echo "GET        /cancelMovement                       controllers.CancelMovementController.onPageLoad()" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "cancelMovement.title = cancelMovement" >> ../conf/messages.en
echo "cancelMovement.heading = cancelMovement" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "cancelMovement.title = cancelMovement" >> ../conf/messages.cy
echo "cancelMovement.heading = cancelMovement" >> ../conf/messages.cy

echo "Migration CancelMovement completed"

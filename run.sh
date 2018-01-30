#!/bin/bash

RUN_CMD="\"run $@\""

#echo $RUN_CMD

bash -c "sbt --error $RUN_CMD"


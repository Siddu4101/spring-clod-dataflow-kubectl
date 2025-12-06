
for app registration
jar file path should be mounted on the container vol
curl -X POST "http://127.0.0.1:61811/apps/task/MY_FIRST_BATCH_JOB?uri=file:///batches/batch-jars/demo-0.0.1-SNAPSHOT.jar"

Task creation
curl -X POST "http://127.0.0.1:58880/tasks/definitions" -d "name=FirstTask" -d "definition=MY_FIRST_BATCH_JOB"

schedule creation
have some issue for schedule (need different image??)
curl -X POST  http://127.0.0.1:60411/tasks/schedules   -d "scheduleName=FirstTaskSchedule"   -d "taskDefinitionName=FirstTask"   -d "properties=scheduler.cron.expression=0 30 17 * * *"
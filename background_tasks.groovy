/*
 Copyright (c) 2019 Nicolas Grué, All rights reserved.
 This library is free software; you can redistribute it and/or modify it under
 the terms of the GNU Lesser General Public License as published by the Free
 Software Foundation; either version 2.1 of the License, or (at your option)
 any later version.
 This library is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 details.
*/
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskManagerUtil;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskConstants;
import com.liferay.portal.background.task.service.BackgroundTaskLocalServiceUtil;

// Resume a task
// BackgroundTaskManagerUtil.resumeBackgroundTask(backgroundTaskId)
//BackgroundTaskManagerUtil.cleanUpBackgroundTasks()
//BackgroundTaskManagerUtil.deleteBackgroundTask(backgroundTaskId)
//BackgroundTaskLocalServiceUtil.resumeBackgroundTask(backgroundTaskId)
/*
allTasks = BackgroundTaskManagerUtil.getBackgroundTasks(0, BackgroundTaskConstants.STATUS_QUEUED);
for(task in allTasks) {
  BackgroundTaskLocalServiceUtil.resumeBackgroundTask(task.getBackgroundTaskId())
}
*/

for (status=BackgroundTaskConstants.STATUS_NEW; status<=BackgroundTaskConstants.STATUS_CANCELLED; status++) {
  allTasks = BackgroundTaskManagerUtil.getBackgroundTasks(0, status);
  println "<p>"+BackgroundTaskConstants.getStatusLabel(status) + ":" + allTasks.size()+"</p> <table>"
  if (allTasks.size()>0) {
  print "<tr><th>backgroundTaskId</th>"
  print "<th>name</th>"
  print "<th>className</th>"
  print "<th>createDate</th>"
  print "<th>completionDate</th>"
  print "<th>userName</th>"
  print "<th>taskExecutorClassName</th>"
  println "<th>statusMessage</th></tr>"
  }
  for(task in allTasks) {
    print "<tr>"
    print "<td>"+task.backgroundTaskId+"</td>"
    print "<td>${task.name}</td>"
    print "<td>${task.taskContextMap.className}</td>"
    print "<td>${task.createDate}</td>"
    print "<td>${task.completionDate}</td>"
    print "<td>${task.model.userName}</td>"
    print "<td>${task.taskExecutorClassName}</td>"
    println "<td>${task.statusMessage}</td>"
    print "</tr>"
  }
  println"</table>"
}

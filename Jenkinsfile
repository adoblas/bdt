node('master') {
  checkout scm 
  echo "Testing JiraSelector"
  def ji = jiraIssueSelector(issueSelector: [$class: 'DefaultIssueSelector'])
  echo "${ji.inspect()}"
}

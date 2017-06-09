node('master') {
  checkout scm 
  def ji = jiraIssueSelector(issueSelector: [$class: 'DefaultIssueSelector'])
  echo "${ji.inspect()}"
}

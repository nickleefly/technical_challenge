import java.net.URLDecoder

def version = '1.0'
def lib = "jenkinsci-lib"

def org = ""
def repo = ""
def branch = ""
def isPR = false

def getBranch() {
  return this.branch
}

def getRepo() {
  return this.repo
}

def getOrg() {
  return this.org
}

def isPR() {
  if(this.branch == null) {
    echo "ERROR:  The branch property was never set on this config object."
    return false
  }
  return branch.startsWith("PR-")
}

def parseGitConfig(jobName) {

  if(jobName == null || jobName.isEmpty())
    return null

  if(jobName.contains("/") && jobName.split("/").length > 2)
    parseGithubOrgFolderGitConfig(jobName)
  else
    parsePipelineGitConfig(jobName)
}

def parseGithubOrgFolderGitConfig(jobName) {
  // Parse out the scm details we need to dynamically build git branches
  echo "Parsing Github Org Folder config"
  if(jobName == null || jobName.isEmpty())
    return null

  def tokens = jobName.tokenize('/')
    this.org = URLDecoder.decode(tokens[tokens.size()-3], "UTF-8")
    this.repo = URLDecoder.decode(tokens[tokens.size()-2], "UTF-8")
    this.branch = URLDecoder.decode(tokens[tokens.size()-1], "UTF-8")
}

def parsePipelineGitConfig(jobName) {
  //TODO: Implement for use within regular pipeline jobs (non github-org-folder plugin pipeline jobs)
  echo "Parsing regular Git Pipeline config"
  return
}

def getConfigPath(String branchName, String configBucket) {
  def bucket = isPR() ? "${configBucket}/config/${repo}/master/*" : "${configBucket}/config/${repo}/${branch}/*"
  return bucket
}

def fetchEnvConfig(String configBucket) {
  bucket = getConfigPath(getBranch(), configBucket)
  sh "gsutil cp -r gs://${bucket} ."
}


return this;

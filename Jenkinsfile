
def config, slack
fileLoader.withGit('git@git-repo-url/jenkins-lib.git', 'common-lib', null, '') {
    config = fileLoader.load('config/cicd-config');
    slack = fileLoader.load('slack/slack-utils');
}

// all the environmen variables can pre-configed in Jenkins UI
// eg, registry usename, password, k8s credentials
stage 'fetch source code'
node {
    try {
      slack.notifyBuild('STARTED')
      if(config.isPR())
        checkout scm
      else
        git url: "git@${repo}:${org}/${repo}", branch: "${branch}"

      unstash 'build-config'
      stash includes: '**', name: 'build-install'
    } catch (e) {
      // If there was an exception thrown, the build failed
      currentBuild.result = "FAILED"
      slack.notifyBuild(currentBuild.result)
      throw e
    }
}

stage 'Build image for current build number'
node {
    try {
        unstash 'build-install'
        sh "docker build -t registry/hello:${env.BUILD_NUMBER} ."
    } catch (e) {
      // If there was an exception thrown, the build failed
        currentBuild.result = "FAILED"
        slack.notifyBuild(currentBuild.result)
        throw e
    }
}

stage 'Push image to registry'
node {
    try {
        sh "docker push registry/hello:${env.BUILD_NUMBER}"
    } catch (e) {
      // If there was an exception thrown, the build failed
        currentBuild.result = "FAILED"
        slack.notifyBuild(currentBuild.result)
        throw e
    }
}

stage 'Remove docker image locally'
node {
    try {
        sh "docker rmi registry/hello:${env.BUILD_NUMBER}"
    } catch (e) {
      // If there was an exception thrown, the build failed
        currentBuild.result = "FAILED"
        slack.notifyBuild(currentBuild.result)
        throw e
    }
}

stage 'Apply Kubernetes deployment'
node {
    try {
        sh 'sed -i.bak "s/{{BUILD_NUMBER}}/$BUILD_NUMBER/g" ./k8s/deployment.yaml'
        sh "kubectl apply -f k8s/deployment.yaml"
    } catch (e) {
      // If there was an exception thrown, the build failed
        currentBuild.result = "FAILED"
        slack.notifyBuild(currentBuild.result)
        throw e
    }
}

echo "Successfully completed Pipeline."

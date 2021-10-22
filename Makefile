export DOCKER_BRIDGE ?= $(shell (uname -a | grep Linux > /dev/null) && echo 172.17.0.1 || echo docker.for.mac.localhost)
export SERVICES = serverless,cloudformation,sts,sqs,dynamodb,s3,sns
export AWS_ACCESS_KEY_ID ?= test
export AWS_SECRET_ACCESS_KEY ?= test
export AWS_DEFAULT_REGION ?= us-east-1
export START_WEB ?= 1
export THUNDRA_APIKEY = 8ae270b5-30c4-4daf-8211-4987e7c31de1
export THUNDRA_AGENT_TEST_PROJECT_ID = 81de7b9f-f432-4264-8f90-b23549363d02
export THUNDRA_AGENT_TRACE_INSTRUMENT_TRACEABLECONFIG = io.thundra.demo.localstack.*.*[traceLineByLine=true]
export PYTHONIOENCODING=utf-8

usage:              ## Show this help
	@fgrep -h "##" $(MAKEFILE_LIST) | fgrep -v fgrep | sed -e 's/\\$$//' | sed -e 's/##//'

install:            ## Install dependencies
	npm install
	which serverless || npm install -g serverless
	which localstack || pip install localstack
	which awslocal || pip install awscli-local

build:              ## Build app
	echo "Building Serverless app ..."
	mvn clean package -DskipTests

test:               ## Test app
	echo "Building Serverless app ..."
	mvn clean test

deploy:             ## Deploy the app locally
	echo "Deploying Serverless app to local environment ..."
	SLS_DEBUG=1 serverless deploy --stage local --region ${AWS_DEFAULT_REGION}

start:              ## Build, deploy and start the app locally
	@make build;
	@make deploy;

deploy-forwarded:   ## Deploy the app locally in forwarded mode
	echo "Deploying Serverless app to local environment ..."
	LAMBDA_FORWARD_URL=http://${DOCKER_BRIDGE}:8080 SLS_DEBUG=1 serverless deploy --stage local --region ${AWS_DEFAULT_REGION} --artifact null.zip

start-embedded:     ## Deploy and start the app embedded in forwarded mode from Localstack
	@make deploy-forwarded;

.PHONY: usage install build test deploy start

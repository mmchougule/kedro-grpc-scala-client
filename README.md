## Kedro gRPC Scala Client

## Requirements:
`sbt version: 1.3.13`

OR 

`scala version: 2.13.3`
## Start sbt shell 
`sbt`


Make sure you have `kedro-grpc-server` installed and running in a kedro project
In your Kedro project:

`kedro server grpc-start`

## Compile project
`sbt compile`

## Run a pipeline and check streaming status of this pipeline run
`sbt run com.example.kedro.KClient`



Kedro gRPC Server installation:

`https://git.mckinsey-solutions.com/Mayur-Chougule/kedro-grpc-server`
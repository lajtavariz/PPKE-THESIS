# yeast
This application is an Information Retrieval system.

## Prerequisites 

Before you can build this project, you must install and configure the following dependencies on your machine:

1. NodeJS v8.11.2 or higher
2. Yarn v1.6.0 or higher
3. NPM v5.6.0 or higher
4. Maven v3.3.9 or higher
5. Java SE Development Kit v1.8.0_171 or higher

## Build & Run

After installing Node, you should be able to run the following command to install development tools.
You will only need to run this command when dependencies change in package.json.

    yarn install

Run the following commands in two separate terminals to create a blissful development experience where your browser
auto-refreshes when files change on your hard drive.

    mvn
    yarn start

Yarn is also used to manage CSS and JavaScript dependencies used in this application. You can upgrade dependencies by
specifying a newer version in package.json. You can also run `yarn update` and `yarn install` to manage dependencies.

The `yarn run` command will list all of the scripts available to run for this project.

## A very brief user manual

After starting the application, you'll be able to upload text content to the system. After uploading some documents, you'll be able to perform a search on them using different similarity measures.

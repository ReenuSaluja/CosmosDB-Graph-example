package Com.MS.CosmosDB.ex;



import org.apache.tinkerpop.gremlin.driver.Client;

import org.apache.tinkerpop.gremlin.driver.Cluster;

import org.apache.tinkerpop.gremlin.driver.Result;

import org.apache.tinkerpop.gremlin.driver.ResultSet;



import java.io.File;

import java.io.FileNotFoundException;

import java.util.List;

import java.util.concurrent.CompletableFuture;

import java.util.concurrent.ExecutionException;



public class App

{

    public static void main( String[] args ) throws ExecutionException, InterruptedException {





        /**

         * There typically needs to be only one Cluster instance in an application.

         */

        Cluster cluster;



        Client client;



        try {

            cluster = Cluster.build(new File("src/remote.yaml")).create();

        } catch (FileNotFoundException e) {

            e.printStackTrace();

            return;

        }



        client = cluster.connect();



        String gremlinQueries[] = new String[] {

            "g.V().drop()",

            "g.addV('person').property('id', 'mohan').property('firstName', 'Mohan').property('age', 44)",

            "g.addV('person').property('id', 'riya').property('firstName', 'Riya').property('lastName', 'Kumar').property('age', 39)",

            "g.addV('person').property('id', 'ram').property('firstName', 'Ram').property('lastName', 'singh')",

            "g.addV('person').property('id', 'rohan').property('firstName', 'Rohan').property('lastName', 'Das')",

            "g.V('mohan').addE('knows').to(g.V('riya'))",

            "g.V('mohan').addE('knows').to(g.V('ram'))",

            "g.V('ram').addE('knows').to(g.V('rohan'))",

            "g.V('mohan').property('age', 44)",

            "g.V().count()",

            "g.V().hasLabel('person').has('age', gt(40))",

            "g.V().hasLabel('person').order().by('firstName', decr)",

            "g.V('mohan').outE('knows').inV().hasLabel('person')",

            "g.V('mohan').outE('knows').inV().hasLabel('person').outE('knows').inV().hasLabel('person')",

            "g.V('mohan').repeat(out()).until(has('id', 'rohan')).path()",

            "g.V('mohan').outE('knows').where(inV().has('id', 'riya')).drop()",

            "g.E().count()",

            "g.V('mohan').drop()" };



        for (String gremlin : gremlinQueries) {

            ResultSet results = client.submit(gremlin);



            CompletableFuture<List<Result>> completableFutureResults = results.all();

            List<Result> resultList = completableFutureResults.get();



            for (Result result : resultList) {

                System.out.println(result.toString());

            }

        }

    }

}

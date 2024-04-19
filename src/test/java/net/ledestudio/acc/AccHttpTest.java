package net.ledestudio.acc;

import net.ledestudio.acc.http.AccHttpRequestResult;
import net.ledestudio.acc.http.AccHttpRequester;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class AccHttpTest {

    @Test
    public void accHttpRequesterUrlConstructorTest() throws ExecutionException, InterruptedException {
        AccHttpRequester requester = new AccHttpRequester("https://play.afreecatv.com/seokwngud/262914674");
        CompletableFuture<AccHttpRequestResult> future = requester.request();
        AccHttpRequestResult result = future.get();

        if (result == null) {
            System.out.println("Cannot found live broadcast information");
            return;
        }
        System.out.println(result.toString());
    }

}

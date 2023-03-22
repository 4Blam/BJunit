import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

public class MyMainClass {
    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
        System.out.println("Started testing");
        boolean running = true;
        int threadsAmount = Integer.valueOf(args[0]);
        System.out.println();
        System.out.println("Threads amount: " + threadsAmount);
        AtomicInteger successTest = new AtomicInteger();
        AtomicInteger failedTest = new AtomicInteger();
        AtomicInteger totalTest = new AtomicInteger();
        ThreadPoolExecutor executor =
                (ThreadPoolExecutor) Executors.newFixedThreadPool(threadsAmount);


        for(int i = 1; i < args.length; i++) {
            System.out.println(args[i].toString());
            Class<?> testedClass = Object.class;
            //select thread
            try {
                testedClass = Class.forName(args[i]);
            } catch (ClassNotFoundException e) {
                System.out.println("No class with name " + args[i]);
                continue;
            }
            Class<?> finalTestedClass = testedClass;
            executor.submit(() -> {
                System.out.println(Thread.currentThread().getName() + " is working with class" +  finalTestedClass.getName() + " now");
                for(Method m: finalTestedClass.getMethods()){
                    if(m.isAnnotationPresent(MyBefore.class)){
                        //System.out.println(m.getName() + " is annotated with before");
                        try {
                            m.invoke(null);
                        } catch (Exception e){
                            System.out.println("Running @before in method " + m.getName() + " failed");
                        }
                    }
                }
                for(Method m: finalTestedClass.getMethods()){
                    if(m.isAnnotationPresent(MyTest.class)){
                        System.out.println(m.getName() + " is annotated with MyTest");
                        try {
                            m.invoke(null);
                            totalTest.getAndIncrement();
                        } catch (Exception e){
                            System.out.println("Test " + m.getName() + " of class " + finalTestedClass.getName() + " failed");
                            failedTest.getAndIncrement();
                            break;
                        }
                        System.out.println("Test " + m.getName() + " of class " + finalTestedClass.getName() + " finished successfully");
                        successTest.getAndIncrement();

                    }
                }
                for(Method m: finalTestedClass.getMethods()){
                    if(m.isAnnotationPresent(MyAfter.class)){
                        //System.out.println(m.getName() + " is annotated with after");
                        try {
                            m.invoke(null);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        } catch (InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
        });
        }
        executor.shutdown();
    }
}

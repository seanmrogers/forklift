package forklift;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import forklift.exception.StartupException;

/**
 * Main ForkLift application instance. ForkLift is started here
 * and stopped here.
 */
public class Forklift {
    private static Logger log = LoggerFactory.getLogger("ForkLift");
    
    public ApplicationContext context;
    
    private boolean classpath;
    private AtomicBoolean running = new AtomicBoolean(false);

    public Forklift() {
        log.debug("Creating ForkLift");

        Runtime.getRuntime().addShutdownHook(new ForkliftShutdown(this));
    }
    
    public synchronized void start() 
      throws StartupException {
        start("services.xml");
    }
    
    public synchronized void start(String resource) 
      throws StartupException {
        log.debug("Initializing Spring Context from Classpath");
        try {
            context = new ClassPathXmlApplicationContext(resource);
        } catch (Exception e) {
            throw new StartupException(e.getMessage());
        }
        ((ClassPathXmlApplicationContext)context).registerShutdownHook();
        
        classpath = true;
        
        running.set(true);
    }
    
    public synchronized void start(File configFile) 
      throws StartupException {
        log.debug("Initializing Spring Context from File {}", configFile.getAbsolutePath());
        try {
            context = new FileSystemXmlApplicationContext("file://" + configFile.getAbsolutePath());
        } catch (Exception e) {
            throw new StartupException(e.getMessage());
        }
        ((FileSystemXmlApplicationContext)context).registerShutdownHook();
        
        classpath = false;
        
        running.set(true);
    }
    
    public void shutdown() {
        if (!running.getAndSet(false))
            return;
        
        if (classpath)
            ((ClassPathXmlApplicationContext)context).close();
        else
            ((FileSystemXmlApplicationContext)context).close();
    }
    
    public boolean isRunning() {
        return running.get();
    }
    
    public ApplicationContext getContext() {
        return context;
    }
    
    public static void main(String args[]) 
      throws StartupException {
        final Forklift forklift = mainWithTestHook(args);
        if (!forklift.isRunning())
            throw new RuntimeException("Unable to start Forklift.");
    }
    
    public static Forklift mainWithTestHook(String args[]) 
      throws StartupException {
        if (args.length != 1) {
            System.err.println("Config file not specified.");
            return null;
        }

        final Forklift forklift = new Forklift();
        final File f = new File(args[0]);
        if (f.exists())
            forklift.start(f);
        else 
            forklift.start(args[0]);
        
        return forklift;
    }
}
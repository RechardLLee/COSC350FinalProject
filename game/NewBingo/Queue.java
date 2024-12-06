// Fig. 21.11: Queue.java
// Queue uses class List.
import java.util.NoSuchElementException;

public class Queue<E> {
   private List<E> queueList;

   // constructor
   public Queue() {queueList = new List<E>("queue");}

   // add object to queue
   public void add(E object) {queueList.insertAtBack(object);}

   // remove object from queue
   public E remove() throws NoSuchElementException {
      return queueList.removeFromFront(); 
   } 

   // determine if queue is empty
   public boolean isEmpty() {return queueList.isEmpty();}


} 

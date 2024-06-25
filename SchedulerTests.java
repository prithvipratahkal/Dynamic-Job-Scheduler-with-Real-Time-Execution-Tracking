import static org.junit.Assert.*;

import org.junit.Test;

public class SchedulerTests {
	
	@Test 
	public void checkCycle() {
		Schedule schedule = new Schedule();
		schedule.insert(8); //adds job 0 with time 8
		Schedule.Job j1 = schedule.insert(3); //adds job 1 with time 3
		schedule.insert(5); //adds job 2 with time 5
		int finishtime = schedule.finish(); //should return 8, since job 0 takes time 8 to complete.
		assertEquals(8, finishtime);
		
		/* Note it is not the earliest completion time of any job, but the earliest the entire set can complete. */
		schedule.get(0).requires(schedule.get(2)); //job 2 must precede job 0
		finishtime = schedule.finish(); //should return 13 (job 0 cannot start until time 5)
		assertEquals(13, finishtime);
		
		schedule.get(0).requires(j1); //job 1 must precede job 0
		finishtime = schedule.finish(); //should return 13
		assertEquals(13, finishtime);
		
		int start = schedule.get(0).start(); //should return 5
		assertEquals(5, start);
		
		assertEquals(0, j1.start()); //should return 0
		start = schedule.get(2).start(); //should return 0
		assertEquals(0, start);
		j1.requires(schedule.get(2)); //job 2 must precede job 1
		
		finishtime = schedule.finish(); //should return 16
		assertEquals(16, finishtime);
		assertEquals(8, schedule.get(0).start());
		assertEquals(5, schedule.get(1).start());
		assertEquals(0, schedule.get(2).start());
		
		schedule.get(1).requires(schedule.get(0)); //job 0 must precede job 1 (creates loop)
		
		assertEquals(-1, schedule.finish());
		assertEquals(-1, schedule.get(0).start());
		assertEquals(-1, schedule.get(1).start());
		assertEquals(0, schedule.get(2).start()); //(no loops in prerequisites)
		assertEquals(16, finishtime);
		assertEquals(16, finishtime);
	}
	
	@Test
	public void noJobsInCycle() {
		Schedule schedule = new Schedule();
		Schedule.Job first = schedule.insert(5);
		Schedule.Job second = schedule.insert(6);
		Schedule.Job third = schedule.insert(3);
		Schedule.Job fourth = schedule.insert(7);
		
		second.requires(third);
		second.requires(fourth);
		first.requires(second);
		
		assertEquals(18, schedule.finish());
		
		Schedule.Job fifth = schedule.insert(15);
		assertEquals(18, schedule.finish());
		
		assertEquals(0, fifth.start());
		assertEquals(0, third.start());
		assertEquals(7, second.start());
		assertEquals(13, first.start());
		
		Schedule.Job sixth = schedule.insert(5);
		sixth.requires(fifth);
		assertEquals(20, schedule.finish());
	}
	
	@Test
	public void fewJobsInCycle() {
		Schedule schedule = new Schedule();
		Schedule.Job first = schedule.insert(1);
		Schedule.Job second = schedule.insert(2);
		Schedule.Job third = schedule.insert(3);
		Schedule.Job fourth = schedule.insert(4);
		Schedule.Job fifth = schedule.insert(5);
		Schedule.Job sixth = schedule.insert(6);
		Schedule.Job seven = schedule.insert(7);
		Schedule.Job eight = schedule.insert(8);
		
		third.requires(first);
		third.requires(second);
		
		// creating a cycle
		third.requires(fifth);
		fourth.requires(third);
		fifth.requires(fourth);
		
		sixth.requires(fourth);
		sixth.requires(fifth);
		
		// seperate connected component
		eight.requires(seven);
		
		assertEquals(0, first.start());
		assertEquals(0, second.start());
		assertEquals(-1, third.start());
		assertEquals(-1, fourth.start());
		assertEquals(-1, fifth.start());
		assertEquals(-1, sixth.start());
		assertEquals(0, seven.start());
		assertEquals(7, eight.start());
		
		assertEquals(-1, schedule.finish());
	}
	
	@Test
	public void allJobsInCycle() {
		Schedule schedule = new Schedule();
		Schedule.Job first = schedule.insert(1);
		Schedule.Job second = schedule.insert(2);
		
		first.requires(second);
		second.requires(first);
		assertEquals(-1, schedule.finish());
		assertEquals(-1, first.start());
		assertEquals(-1, second.start());
	}
}

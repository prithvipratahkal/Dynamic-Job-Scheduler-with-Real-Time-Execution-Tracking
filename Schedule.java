 import java.util.Arrays;

public class Schedule {
	
	DynamicArray<Job> jobs;
	boolean isGraphChanged;
	DynamicArray<Integer> topoOrder;
	int finishTime;
	
	public Schedule() {
		jobs = new DynamicArray<>();
		isGraphChanged = true;
		topoOrder = new DynamicArray<>();
		finishTime = Integer.MIN_VALUE;
	}
	
	public Job insert(int time) {
		Job job = new Job(time);
		this.jobs.add(job);
		
		this.isGraphChanged = true;
		return job;
	}
	
	public Job get(int index) {
		return this.jobs.get(index);
	}

	public int finish() {
		if (!this.isGraphChanged) {
			return finishTime;
		}
				
		if (!findTopologicalOrder()) {
			this.finishTime = -1;
			return -1;
		}
		
		dagShortestPath();
		
		return finishTime;
	}
	
	private void resetFields() {
		for (int i = 0; i < jobs.size(); i++) {
			Job job = jobs.get(i);
			job.startTime = 0;
			job.isInCycle = true;
		}
		
		this.topoOrder.clear();
		
		this.finishTime = Integer.MIN_VALUE;
	}
	
	boolean findTopologicalOrder() {
		resetFields();
		
		// clone the Indegree list
		int[] indegree = new int[jobs.size()];
		
		for (int i = 0; i < jobs.size(); i++) {
			Job job = jobs.get(i);
			indegree[i] = job.indegree;
			if (job.indegree == 0) {
				this.topoOrder.add(i);
			}
		}

		if (this.topoOrder.isEmpty()) {
			return false;
		}
		
		for (int current = 0; current < topoOrder.size(); current++) {
			int currentTopo = topoOrder.get(current);
			DynamicArray<Integer> outEdges = jobs.get(currentTopo).outgoingEdges;
			
			for (int to = 0; to < outEdges.size(); to++) {
				int edgeTo = outEdges.get(to);
				indegree[edgeTo] -= 1;
				
				if (indegree[edgeTo] == 0) {
					topoOrder.add(edgeTo);
				}
			}
		}
				
		if (this.topoOrder.size() != this.jobs.size()) {
			return false;
		}
		
		return true;
	}
	
	void dagShortestPath() {		
		for (int current = 0; current < topoOrder.size(); current++) {
			Job currentJob = jobs.get(topoOrder.get(current));
			DynamicArray<Integer> outEdges = currentJob.outgoingEdges;
			
			for (int to = 0; to < outEdges.size(); to++) {
				Job toJob = jobs.get(outEdges.get(to));
				int time = currentJob.startTime + currentJob.duration;
				if (toJob.startTime < time) {
					toJob.startTime = time;
				}
			}
			
			currentJob.isInCycle = false;
		}
		
		for (int i = 0; i < jobs.size(); i++) {
			int startTime = jobs.get(i).startTime;
			int duration = jobs.get(i).duration;
			if (finishTime < startTime + duration) {
				finishTime = startTime + duration;
			}
		}
		
		for (int i = 0; i < jobs.size(); i++) {
			Job job = jobs.get(i);
			if (job.isInCycle) {
				finishTime = -1;
				job.startTime = -1;
			}
		}
		
		this.isGraphChanged = false;
	}
	
	class Job {
		int startTime;
		int duration;
		DynamicArray<Integer> outgoingEdges;
		boolean isInCycle;
		int indegree;
		
		private Job(int duration) {
			startTime = 0;
			this.duration = duration;
			outgoingEdges = new DynamicArray<>();
			isInCycle = true;
			indegree = 0;
		}
		
		public void requires(Job from) {
			//add the index
			int to = jobs.indexOf(this);
			from.outgoingEdges.add(to);
			isGraphChanged = true;
			
			Job job = jobs.get(to);
			job.indegree += 1;
		}
		
		public int start() {
			if (!isGraphChanged) {
				return this.startTime;
			}
			
			findTopologicalOrder(); 
			  
			dagShortestPath();
			
			return this.startTime;
		}
	}
	
	public class DynamicArray<T> {
		Object[] array;
		int size;
		int capacity;
		
		DynamicArray() {
			initialize();
		}
		
		public void add(Object obj) {
			checkForArrayFull();
			
			this.array[size] = obj;
			this.size++;
		}
		
		private void initialize() {
			capacity = 10;
			array = new Object[this.capacity];
			size = 0;
		}
		
		private void checkForArrayFull() {
			if (this.size == this.capacity) {
				this.capacity *= 2;
				Object[] newArray = new Object[this.capacity];
				copyOldArrayToNewArray(this.array, newArray);
				
				this.array = newArray;
			}
		}

		private void copyOldArrayToNewArray(Object[] oldArray, Object[] newArray) {
			for (int i = 0; i < oldArray.length; i++) {
				newArray[i] = oldArray[i];
			}	
		}
		
		public int size() {
			return this.size;
		}
		
		public T get(int index) {
			return (T) this.array[index];
		}
		
		public void clear() {
			initialize();
		}
		
		public boolean isEmpty() {
			return this.size == 0 ? true : false;
		}
		
		public int indexOf(Object obj) {
			for (int i = 0; i < this.array.length; i++) {
				if (obj.equals(this.array[i])) {
					return i;
				}
			}
			
			return -1;
		}
	}
}

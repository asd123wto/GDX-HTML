package team.rpsg.html.util;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.List;

/**
 * 延时运行工具<br>
 * LibGDX本身也有个{@link Timer}工具，但是他的实现是靠多线程的，为了避免吃到意外的屎，只好造一个轮子了2333，该轮子不依靠线程，而是在主线程的主循环里工作。
 */
public class Timer {
    List<Task> list = new ArrayList<>();
    List<Task> addList = new ArrayList<>();

    public static final int FOREVER = -1;
	boolean pause = false;
	
	public Task add(TimeType type, int delay, int repeat, Runnable run) {
		Task task = new Task();
		task.run = run;
		task.time = task.originTime = delay;
		task.repeat = repeat;
		task.type = type;
		addList.add(task);
		
		return task;
	}
	
	public Task add(TimeType type, int delay, Runnable run) {
		return add(type, delay, 1, run);
	}
	
	public void act() {
		float delta = Gdx.graphics.getRawDeltaTime();

		if(!addList.isEmpty()){
			list.addAll(addList);
			addList.clear();
		}

		if(!list.isEmpty()){
			List<Task> removeList = new ArrayList<>();
			for(Task timer : list){
				if((timer.type == TimeType.frame && timer.time -- < 0) || (timer.type == TimeType.millisecond && (timer.time -= delta * 1000) < 0)){
					timer.run.run();
					if(timer.repeat != FOREVER &&  -- timer.repeat == 0)
						removeList.add(timer.done());
					else
						timer.time = timer.originTime;
				}
			}

			list.removeAll(removeList);
		}
	}

	public void pause() {
		pause = true;
	}

	public void resume() {
		pause = false;
	}

	public void remove(Task timer) {
		list.remove(timer);
	}

    public void then(Runnable o) {
		add(TimeType.frame, 0, o);
    }

    public static class Task{
		public int time, originTime;
		public int repeat = 1;
		public Runnable run;
		public TimeType type;

		public boolean done = false;

		public Task done(){
			done = true;
			return this;
		}
	}

	public enum TimeType{
		frame, millisecond
	}
}

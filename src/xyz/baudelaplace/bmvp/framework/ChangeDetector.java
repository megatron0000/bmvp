package xyz.baudelaplace.bmvp.framework;

public abstract class ChangeDetector {
	abstract void run();
	abstract CDStrategy getStrategy();
	abstract void setStrategy(CDStrategy strategy);
}

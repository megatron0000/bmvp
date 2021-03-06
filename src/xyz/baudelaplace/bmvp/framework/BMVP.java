
package xyz.baudelaplace.bmvp.framework;

import java.util.concurrent.CompletableFuture;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;

import xyz.baudelaplace.bmvp.framework.annotations.Root;
import xyz.baudelaplace.bmvp.framework.annotations.Strategy;
import xyz.baudelaplace.bmvp.framework.views.ViewAdapter;

/**
 * @author vitor
 *
 */
public abstract class BMVP extends AbstractModule {

	private MasterPresenter masterPresenter;

	private Class<? extends Presenter<?>> rootPresenterClass;

	private ViewAdapter adapter;

	protected BMVP(Class<? extends Presenter<?>> rootPresenterClass, ViewAdapter adapter) {
		this.rootPresenterClass = rootPresenterClass;
		this.adapter = adapter;
	}

	@Override
	protected void configure() {
		internalConfigure();
		configureBindings();
	}

	public abstract void configureBindings();

	void internalConfigure() {
		bind(ComponentProvider.class).in(Scopes.SINGLETON);
		bind(EventDispatcher.class).in(Scopes.SINGLETON);
		bind(new TypeLiteral<Class<? extends Presenter<?>>>() {
		}).annotatedWith(Root.class).toInstance(rootPresenterClass);
		bind(CDStrategy.class).annotatedWith(Strategy.Default.class).toInstance(CDStrategyEnum.Default);
		bind(CDStrategy.class).annotatedWith(Strategy.OnPush.class).toInstance(CDStrategyEnum.OnPush);
		bind(ViewAdapter.class).toInstance(adapter);
	}

	public CompletableFuture<Void> start() {
		Injector injector = Guice.createInjector(this);
		EventDispatcher eventDispatcher = injector.getInstance(EventDispatcher.class);
		ComponentProvider compProvider = injector.getInstance(ComponentProvider.class);

		eventDispatcher.setOnStart(
				() -> masterPresenter =
						(MasterPresenter) compProvider.getPresenter(MasterPresenter.class, null));

		eventDispatcher.setOnQueueEmpty(() -> masterPresenter.getChangeDetector().run());

		eventDispatcher.setDispatchAlgorithm(
				(queuedEvent) -> queuedEvent.getEvent().getStrategy().run(
						queuedEvent, masterPresenter.getSubtree(), eventDispatcher));

		return eventDispatcher.start();

	}

}

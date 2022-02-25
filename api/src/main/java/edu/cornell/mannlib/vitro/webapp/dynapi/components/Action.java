package edu.cornell.mannlib.vitro.webapp.dynapi.components;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.cornell.mannlib.vitro.webapp.dynapi.OperationData;
import edu.cornell.mannlib.vitro.webapp.utils.configuration.Property;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Action implements RunnableComponent, Poolable {

	private static final Log log = LogFactory.getLog(Action.class);

	private Step firstStep = null;
	private RPC rpc;

	private Set<Long> clients = ConcurrentHashMap.newKeySet();

	@Override
	public void dereference() {
		if (firstStep != null) {
			firstStep.dereference();
			firstStep = null;
		}
		rpc.dereference();
		rpc = null;
	}

	public OperationResult run(OperationData input) {
		if (firstStep == null) {
			return new OperationResult(HttpServletResponse.SC_NOT_IMPLEMENTED);
		}
		return firstStep.run(input);
	}

	@Property(uri = "https://vivoweb.org/ontology/vitro-dynamic-api#firstStep", minOccurs = 1, maxOccurs = 1)
	public void setStep(OperationalStep step) {
		this.firstStep = step;
	}

	@Property(uri = "https://vivoweb.org/ontology/vitro-dynamic-api#assignedRPC", minOccurs = 1, maxOccurs = 1)
	public void setRPC(RPC rpc) {
		this.rpc = rpc;
	}

	@Override
	public String getName() {
		return rpc.getName();
	}

	@Override
	public boolean isValid() {
		return true;
	}

	@Override
	public void addClient() {
		clients.add(Thread.currentThread().getId());
	}

	@Override
	public void removeClient() {
		clients.remove(Thread.currentThread().getId());
	}

	@Override
	public void removeDeadClients() {
		Map<Long, Boolean> currentThreadIds = Thread
				.getAllStackTraces()
				.keySet()
				.stream()
				.collect(Collectors.toMap(Thread::getId, Thread::isAlive));
		for (Long client : clients) {
			if (!currentThreadIds.containsKey(client) || currentThreadIds.get(client) == false) {
				clients.remove(client);
			} 
		}
	}

	@Override
	public boolean hasClients() {
		return !clients.isEmpty();
	}

}
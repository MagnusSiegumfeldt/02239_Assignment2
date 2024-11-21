package auth.session;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class SessionProxy implements InvocationHandler, Serializable {
  private final Object target;
  private final transient ISessionManager sessionManager;

  public SessionProxy(Object target, ISessionManager sessionManager) {
    this.target = target;
    this.sessionManager = sessionManager;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    if (!this.sessionManager.checkSessionValid((String) args[0])) {
      throw new RemoteException("Session not validated");
    }

    return method.invoke(target, args);
  }

  @SuppressWarnings("unchecked")
  public static <T extends Remote> T createProxy(T target, ISessionManager sessionManager, Class<T> interfaceType)
      throws RemoteException {
    T proxy = (T) Proxy.newProxyInstance(
        interfaceType.getClassLoader(),
        new Class<?>[] { interfaceType },
        new SessionProxy(target, sessionManager));

    return (T) UnicastRemoteObject.exportObject(proxy, 0);
  }
}

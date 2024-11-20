package auth.roles;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import auth.session.ISessionManager;

public class AccessControlProxy implements InvocationHandler, Serializable {
  private final Object target;
  private transient final IAccessControlManager accessControl;
  private transient final ISessionManager sessionManager;

  public AccessControlProxy(Object target, IAccessControlManager accessControl, ISessionManager sessionManager) {
    this.target = target;
    this.accessControl = accessControl;
    this.sessionManager = sessionManager;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    accessControl.check(sessionManager.getCurrentUser(), method.getName());
    return method.invoke(target, args);
  }

  @SuppressWarnings("unchecked")
  public static <T extends Remote> T createProxy(T target, IAccessControlManager accessControl,
      ISessionManager sessionManager, Class<T> interfaceType)
      throws RemoteException {
    T proxy = (T) Proxy.newProxyInstance(
        interfaceType.getClassLoader(),
        new Class<?>[] { interfaceType },
        new AccessControlProxy(target, accessControl, sessionManager));

    return (T) UnicastRemoteObject.exportObject(proxy, 0);
  }
}

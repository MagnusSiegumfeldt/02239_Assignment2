package auth.session;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import auth.password.IPasswordManager;

public class SessionProxy implements InvocationHandler, Serializable {
  private final Object target;
  private transient final ISessionManager sessionManager;

  public SessionProxy(Object target, ISessionManager sessionManager) {
    this.target = target;
    System.out.println("param" + sessionManager == null);
    this.sessionManager = sessionManager;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

    String currentUser = this.sessionManager.getCurrentUser();
    if (currentUser == null || currentUser.isEmpty() || !this.sessionManager.checkSessionPeriod(currentUser)) {
      throw new RemoteException("User is not logged in");
    }

    return method.invoke(target, args);
  }

  @SuppressWarnings("unchecked")
  public static <T extends Remote> T createProxy(T target, ISessionManager sessionManager,
      IPasswordManager passwordManager, Class<T> interfaceType)
      throws RemoteException {
    T proxy = (T) Proxy.newProxyInstance(
        interfaceType.getClassLoader(),
        new Class<?>[] { interfaceType },
        new SessionProxy(target, sessionManager));

    return (T) UnicastRemoteObject.exportObject(proxy, 0);
  }
}

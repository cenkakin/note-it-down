export function setUser(user) {
  localStorage.setItem('user', JSON.stringify(user));
}

export function removeUser() {
  localStorage.removeItem('user');
}

export function getUser() {
  return JSON.parse(localStorage.getItem('user'));
}

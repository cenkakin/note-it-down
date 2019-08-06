export function setUserWrapper(userWrapper) {
  localStorage.setItem('userWrapper', JSON.stringify(userWrapper));
}

export function removeUserWrapper() {
  localStorage.removeItem('userWrapper');
}

export function getUserWrapper() {
  return JSON.parse(localStorage.getItem('userWrapper'));
}

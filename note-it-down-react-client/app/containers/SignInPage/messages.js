/*
 * HomePage Messages
 *
 * This contains all the text for the HomePage component.
 */
import { defineMessages } from 'react-intl';

export const scope = 'noteitdown.containers.SignInPage';

export default defineMessages({
  header: {
    id: `${scope}.forgot_password.header`,
    defaultMessage: 'Sign In',
  },
  forgotPassword: {
    id: `${scope}.forgot_password.message`,
    defaultMessage: 'Forgot Password?',
  },
  forwardSignUp: {
    id: `${scope}.forgot_password.message`,
    defaultMessage: "Don't have an account? Sign Up",
  },
  rememberMe: {
    id: `${scope}.remember_me.message`,
    defaultMessage: 'Remember me',
  },
  signInButton: {
    id: `${scope}.sign_in_button.message`,
    defaultMessage: 'Sign In',
  },
});

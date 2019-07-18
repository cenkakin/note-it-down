import { defineMessages } from 'react-intl';

export const scope = 'noteitdown.containers.SignUpPage';

export default defineMessages({
  header: {
    id: `${scope}.forgot_password.header`,
    defaultMessage: 'Sign Up',
  },
  signUpButton: {
    id: `${scope}.sign_up_button.message`,
    defaultMessage: 'Sign Up',
  },
  forwardLogin: {
    id: `${scope}.forward_login.message`,
    defaultMessage: 'Already have an account?',
  },
});

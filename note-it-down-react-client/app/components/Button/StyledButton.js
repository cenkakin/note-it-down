import styled from 'styled-components';

import Button from '@material-ui/core/Button';

const StyledButton = styled(Button)`
  && {
    margin: ${props => props.theme.spacing(3, 0, 2)};
  }
`;

export default StyledButton;

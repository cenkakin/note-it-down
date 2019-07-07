import styled from 'styled-components';
import { Avatar } from '@material-ui/core';

const StyledAvatar = styled(Avatar)`
  && {
    margin: ${props => props.theme.spacing(1)}px;
    background-color: ${props => props.theme.palette.secondary.main};
  }
`;

export default StyledAvatar;

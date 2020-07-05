import styled from 'styled-components';
import Paper from '@material-ui/core/Paper';

const StyledPaper = styled(Paper)`
  height: 100%;
  min-height: ${props => props.theme.spacing(100)}px;
  margin: ${props => props.theme.spacing(0, 30, 3, 30)};
  padding: ${props => props.theme.spacing(3, 7)};
  display: 'flex';
  flex-direction: 'column';
  line-height: 1.9;
`;

export default StyledPaper;

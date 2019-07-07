import styled from 'styled-components';

const Paper = styled.div`
  display: flex;
  margin-top: ${props => props.theme.spacing(8)}px;
  align-items: center;
  flex-direction: column;
`;

export default Paper;

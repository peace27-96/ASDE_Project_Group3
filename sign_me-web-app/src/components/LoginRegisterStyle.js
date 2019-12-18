import { makeStyles } from '@material-ui/core/styles';
import { height } from '@material-ui/system';

export const useStyles = makeStyles(theme => ({
    paper: {
      marginTop: theme.spacing(8),
      display: 'flex',
      flexDirection: 'column',
      alignItems: 'center',
    },
    avatar: {
      /*margin: theme.spacing(1),
      backgroundColor: theme.palette.secondary.main,*/
    },
    logo: {
      width:'150px',
      height: '150px'

    },
    buttonStyle: {
      color: 'white',
      margin: theme.spacing(3, 0, 2),
      background: 'linear-gradient(45deg, #009569 30%, #008B5D 90%)',
    },
    form: {
      width: '100%',
      marginTop: theme.spacing(3),
    },
  }));
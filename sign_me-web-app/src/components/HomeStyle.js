import { makeStyles } from '@material-ui/core/styles';

export const useStyles = makeStyles(theme => ({

  logo: {
    width: '150px',
    height: '150px'
  },
  summaryField: {
    "margin": "5px",
  },

  summaryCard: {
    'margin-top': '20px',
    padding: '5px',
    maxWidth: 345,
  },
  coursesCard: {
    width: '100%',
    height: '650px',
  },
  coursesContainer: {
    height: "600px",
    "overflow-y": "auto",
    padding: "5px"
  },
  coursesList: {
    maxWidth: '100%'
  },
  courseItem: {
    margin: '0%'
  },
  courseAttendace: {
    width: 'calc(25% - 1px)',
    height: '100%',
    display: "inline-block",
    "border-left": "1px solid black",
    textAlign: "center"
  },
  courseAttendaceHeader: {
    "font-weight": "bold",
    width: '25%',
    height: '100%',
    display: "inline-block",
    textAlign: "center"
  },
  courseNameHeader: {
    "font-weight": "bold",
    //width: "200px",
    "text-transform": "none",
    width: 'calc(75% - 5px)',
    display: "inline-block",
    "padding": "0 0 0 5px",
    textAlign: "center",
  },
  courseName: {
    //width: "200px",

    "text-transform": "none",
    width: 'calc(75% - 5px)',
    display: "inline-block",
    "padding": "0 0 0 5px",
    textAlign: "left",
  },
  profileContainer: {
    maxHeight: 700,
  },
  pictureCard: {
    padding: '5px',
    maxWidth: 345,
  },
  media: {
    height: "100%",
    paddingTop: '56.25%', // 16:9
  },

  createdCourseNameHeader: {
    "font-weight": "bold",
    //width: "200px",
    "text-transform": "none",
    display: "inline-block",
    "padding": "0 0 0 25px",
    textAlign: "center",
  },
  homeButton: {
    "max-width":"150px",
    width: '100% !important',
    color: 'white',
    margin: theme.spacing(3, 0, 2),
    background: 'linear-gradient(45deg, #009569 30%, #008B5D 90%)'
  },
  buttonStyle: {
    color: 'white',
    margin: theme.spacing(3, 0, 2),
    background: 'linear-gradient(45deg, #009569 30%, #008B5D 90%)',
  },
  picture: {
    height: "100%",
    paddingTop: '56.25%',
    "display" : "block",
    "background-size" : "contain", 
    "background-repeat" : "no-repeat",
    "background-position" : "center"
  },

}));
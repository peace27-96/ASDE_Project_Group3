import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Grid from '@material-ui/core/Grid';
import LectureItems from './LectureItems';
import NoticesList from './NoticesList';
import MaterialList from './MaterialList';
import LectureCreation from './LectureCreation'
import StudentSubscriptions from './StudentSubscriptions'
import Cookies from 'js-cookie'
import { createBrowserHistory } from "history";
import BaseInstance from '../http-client/BaseInstance.js';

const useStyles = makeStyles(theme => ({
    root: {
        flexGrow: 1,
    },
    paper: {
        padding: theme.spacing(2),
        textAlign: 'center',
        color: theme.palette.text.secondary,
    },
}));



export default function FullWidthGrid({history}) {
    
    //console.log(history)
    if(history === undefined) {
        history = createBrowserHistory()
        history.push("/login")
    }


    if(Cookies.get("email") === undefined || Cookies.get("currentCourse") === undefined)  
        history.push("/login")

    const lecturerId = Cookies.get("lecturerId");
    const currentUserId = Cookies.get("email");
    const classes = useStyles();
    const [subscribedStudents, setSubscribedStudents] = React.useState(JSON.parse(Cookies.get("currentStudents")))
    const [notices, setNotices] = React.useState(JSON.parse(Cookies.get("courseNotices")))
    const [material, setMaterial] = React.useState(JSON.parse(Cookies.get("material")))

    // Se sono il professore
    if(lecturerId === currentUserId) {
        return (
            <div className={classes.root} >
                <h1 style={{ paddingLeft: 15, display:"inline-block"}}>{JSON.parse(Cookies.get("currentCourse")).subject}</h1>
                <Grid container justify="center" xs={12} direction="row">
                    <Grid item xs={9} style={{paddingRight:30}}>
                        <LectureItems material={material} setMaterial={setMaterial} setNotices={setNotices} subscribedStudents={subscribedStudents} setSubscribedStudents={setSubscribedStudents}/>                  
                    </Grid>
                    <Grid container xs={3} direction="column" spacing={3}>
                        <NoticesList notices={notices} setNotices={setNotices}/>
                        <MaterialList material={material} setMaterial={setMaterial} />
                        <StudentSubscriptions subscribedStudents={subscribedStudents} setSubscribedStudents={setSubscribedStudents}/>
                    </Grid>
                </Grid>
            </div>
        );
    } else { // Se sono lo studente
        return (
            <div className={classes.root} >
                <h1 style={{ paddingLeft: 15, display:"inline-block"}}>{JSON.parse(Cookies.get("currentCourse")).subject}</h1>
                <Grid container justify="center" xs={12} direction="row">
                    <Grid item xs={9} style={{paddingRight:30}}>
                        <LectureItems setNotices={setNotices} subscribedStudents={subscribedStudents} setSubscribedStudents={setSubscribedStudents}/>                  
                    </Grid>
                    <Grid container xs={3} direction="column" spacing={3}>
                        <NoticesList notices={notices} setNotices={setNotices}/>
                        <MaterialList material={material} setMaterial={setMaterial} />
                    </Grid>
                </Grid>
            </div>
        );
    }
}
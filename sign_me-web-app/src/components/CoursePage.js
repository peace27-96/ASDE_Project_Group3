import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Grid from '@material-ui/core/Grid';
import LectureItems from './LectureItems';
import FolderList from './FolderList';
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
    console.log("CoursePage")
    console.log(history)
    if(history === undefined) {
        history = createBrowserHistory()
        history.push("/login")
    }


    if(Cookies.get("email") === undefined || Cookies.get("currentCourse") === undefined)  
        history.push("/login")

    const classes = useStyles();

    return (
        <div className={classes.root} >
            <h1 style={{ paddingLeft: 15, display:"inline-block"}}>{JSON.parse(Cookies.get("currentCourse")).subject}</h1>
            <Grid container justify="center" xs={12} direction="row">
                <Grid item xs={9} style={{paddingRight:30}}>
                    <LectureItems/>                  
                </Grid>
                <Grid container xs={3} direction="column" spacing={3}>
                    {/*<FolderList type="Avviso"/>
                    <FolderList type="Materiale"/>*/}
                    <StudentSubscriptions/>
                </Grid>
            </Grid>
        </div>
    );
}
import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Grid from '@material-ui/core/Grid';
import LectureItems from './LectureItems';
import FolderList from './FolderList';
import { Button } from '@material-ui/core';
import LectureCreation from './LectureCreation'

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

export default function FullWidthGrid() {
    const classes = useStyles();

    return (
        <div className={classes.root} >
            <h1 style={{ paddingLeft: 15, display:"inline-block"}}>Agile Software Development - Lecturer View</h1>
            <LectureCreation />
            <Grid container justify="center" xs={12} direction="row">
                <Grid item xs={9} style={{paddingRight:30}}>
                    <LectureItems/>
                </Grid>
                <Grid container xs={3} direction="column" spacing={3}>
                    <FolderList type="Avviso"/>
                    <FolderList type="Materiale"/>
                </Grid>
            </Grid>
        </div>
    );
}
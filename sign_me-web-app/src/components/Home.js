import React from 'react';
import Card from '@material-ui/core/Card';
import CardMedia from '@material-ui/core/CardMedia';
import CardContent from '@material-ui/core/CardContent';
import Typography from '@material-ui/core/Typography';
import Button from '@material-ui/core/Button';
import { useStyles } from './HomeStyle.js';
import Grid from '@material-ui/core/Grid';
import Divider from '@material-ui/core/Divider';
import TabPanel from './TabPanel';
import BaseInstance from '../http-client/BaseInstance'
import CourseCreation from './CourseCreation'
import Cookies from 'js-cookie'
import { createBrowserHistory } from "history";
import Fab from '@material-ui/core/Fab';

export default function RecipeReviewCard({ history }) {

    if (history === undefined) {
        history = createBrowserHistory()
        history.push("/login")
    }

    if (Cookies.get("email") === undefined)
        history.push("/login");

    const classes = useStyles();
    const [expanded, setExpanded] = React.useState(false);
    // Cookies.remove("currentCourse")

    var imagePath = Cookies.get("profilePicture")
    if (imagePath === null) imagePath = '../resources/user-icon.png'
    else imagePath = 'http://localhost:8080/signme/' + imagePath

    const onFileChangeHandler = (e) => {
        e.preventDefault();
        console.log("upload")
        const formData = new FormData();
        var nameFile = e.target.files[0].name;
        var extension = nameFile.match(/.*\.(\w+)/)[1];
        nameFile = Cookies.get("email") + "." + extension.toLowerCase();
        formData.append('file', e.target.files[0], nameFile);
        BaseInstance.post("updateProfilePicture", formData)
            .then(res => {
                console.log(res.data);
                if (res.data === "Wrong extension") {
                    alert("wrong extension")
                    return
                }
                if (res.data === "Failed") {
                    alert("generic error")
                    return
                }
                imagePath = res.data
                Cookies.set("profilePicture", imagePath)
                window.location.reload(false);
            })
    };

    return (
        <div className={classes.profileContainer} style={{ padding: 20 }}>
            <Grid container>
                <Grid item xs={3}>
                    <Card className={classes.pictureCard}>
                        <CardMedia className={classes.media} image={imagePath} title="User Icon" />
                    </Card>
                    <Card className={classes.summaryCard}>
                        <div style={{ display: "flex", "justify-content": "space-between" }}>
                            <Typography className={classes.summaryField}>Name: </Typography>
                            <Typography className={classes.summaryField}>{Cookies.get("firstName")}</Typography>
                        </div>
                        <Divider></Divider>
                        <div style={{ display: "flex", "justify-content": "space-between" }}>
                            <Typography className={classes.summaryField}>Surname: </Typography>
                            <Typography className={classes.summaryField}>{Cookies.get("lastName")}</Typography>
                        </div>
                        <Divider></Divider>
                        <div style={{ display: "flex", "justify-content": "space-between" }}>
                            <Typography className={classes.summaryField}>Email: </Typography>
                            <Typography className={classes.summaryField}>{Cookies.get("email")}</Typography>
                        </div>
                    </Card>
                    <Fab component="label" className={classes.buttonStyle} color="primary" variant="extended">
                        Update Pic
                        <input type="file" style={{ display: "none" }} onChange={onFileChangeHandler}
                            accept=".jpg,.jpeg,.png,.bmp" />
                    </Fab>
                    <CourseCreation />
                </Grid>

                <Grid item xs={9} style={{ paddingLeft: "20px" }}>
                    <Card className={classes.coursesCard}>
                        <TabPanel />
                    </Card>
                </Grid>
            </Grid>
        </div>
    );
}
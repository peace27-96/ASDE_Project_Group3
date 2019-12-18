import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import clsx from 'clsx';
import Card from '@material-ui/core/Card';
import CardHeader from '@material-ui/core/CardHeader';
import CardMedia from '@material-ui/core/CardMedia';
import CardContent from '@material-ui/core/CardContent';
import CardActions from '@material-ui/core/CardActions';
import Collapse from '@material-ui/core/Collapse';
import Avatar from '@material-ui/core/Avatar';
import IconButton from '@material-ui/core/IconButton';
import Typography from '@material-ui/core/Typography';
import { red } from '@material-ui/core/colors';
import FavoriteIcon from '@material-ui/icons/Favorite';
import ShareIcon from '@material-ui/icons/Share';
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import MoreVertIcon from '@material-ui/icons/MoreVert';
import Button from '@material-ui/core/Button';
import BaseInstance from '../http-client/BaseInstance'

const useStyles = makeStyles(theme => ({
    card: {
        maxWidth: 345,
    },
    media: {
        height: 0,
        paddingTop: '56.25%', // 16:9
    },
    expand: {
        transform: 'rotate(0deg)',
        marginLeft: 'auto',
        transition: theme.transitions.create('transform', {
            duration: theme.transitions.duration.shortest,
        }),
    },
    expandOpen: {
        transform: 'rotate(180deg)',
    },
    avatar: {
        backgroundColor: red[500],
    },
}));

export default function RecipeReviewCard() {
    const classes = useStyles();
    const [expanded, setExpanded] = React.useState(false);

    const handleExpandClick = () => {
        setExpanded(!expanded);
    };


    const onFileChangeHandler = (e) => {
        e.preventDefault();
        console.log("upload")
        const formData = new FormData();
        var nameFile = e.target.files[0].name;
        console.log(nameFile)
        var extension = nameFile.match(/.*\.(\w+)/)[1];
        nameFile = "email." + extension
        formData.append('file', e.target.files[0], nameFile);
       BaseInstance.post("updatePicture", formData)
            .then(res => {
                console.log(res.data);
                alert("File uploaded successfully.")
            }).catch(res => {
                console.log(res)
                if(res==="Wrong extension") alert("wrong extension")
            })
    };

    return (
        <div>
            <div style={{ padding: 20 }}>
                <Card className={classes.card}>
                    <CardMedia
                        className={classes.media}
                        image={require('../resources/user-icon.png')}
                        title="User Icon"
                    />
                    <CardContent>
                        <Button /*onClick={this.upload}*/ component="label">
                            ciao
                            <input type="file" style={{ display: "none" }} onChange={onFileChangeHandler} 
                            accept=".jpg,.jpeg,.png,.bmp"/>
                        </Button>
                    </CardContent>

                </Card>
            </div>
        </div>
    );
}
